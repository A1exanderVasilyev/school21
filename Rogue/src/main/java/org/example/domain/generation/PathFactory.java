package org.example.domain.generation;

import java.util.*;

public class PathFactory {
    private Integer roomsCount;

    public PathFactory(Integer roomsCount){
        this.roomsCount = roomsCount;
    }

    public List<Integer[]> generatePaths(Integer startIndex){
        List<Integer[]> paths = basicPath(startIndex);
        addPath(paths);

        return paths;
    }

    private List<Integer> getNeighbours(Integer index){
        List<Integer> neighbours = new LinkedList<>();
        Integer size = (int) Math.sqrt(roomsCount);
        if(index - size >= 0){
            neighbours.add(index - size);
        }
        if(index + 1 <= size && (index + 1) % size != 0){
            neighbours.add(index + 1);
        }
        if(index + size < size*size){
            neighbours.add(index + size);
        }
        if(index - 1 >= 0 && index % size != 0){
            neighbours.add(index - 1);
        }
        return neighbours;
    }

    private Integer getUnvisitedRoom(Integer index, List<Integer> visitedRooms, List<Integer[]> path){
        Integer neighbour = -1;
        Integer isStop = 0;
        Integer curInex = index;
        List<Integer> neighbours = getNeighbours(index);
        while (isStop == 0){
            Integer neighbourIndex = (int) (Math.random() * neighbours.size());
            neighbour = neighbours.get(neighbourIndex);
            if(visitedRooms.contains(neighbour)){
                neighbours.remove(neighbour);
                if(neighbours.isEmpty()){
                    curInex = visitedRooms.indexOf(index);
                    if(curInex <= 0){
                        neighbour = -1;
                        isStop = 1;
                    }else{
                        curInex = curInex - 1;
                        index = visitedRooms.get(curInex);
                        neighbours = getNeighbours(index);
                    }
                }
            }else{
                visitedRooms.add(neighbour);
                Integer[] pairIndex = new Integer[]{index, neighbour};
                Arrays.sort(pairIndex);
                path.add(pairIndex);
                isStop = 1;
            }
        }
        return neighbour;
    }

    private List<Integer[]> basicPath(Integer startIndex){
        List<Integer> visitedRooms = new ArrayList<>();
        List<Integer[]> path = new ArrayList<>();

        Integer curIndex = startIndex;
        visitedRooms.add(curIndex);

        for(int i = 0; i < roomsCount - 1;i++){
            curIndex = getUnvisitedRoom(curIndex, visitedRooms, path);
            if(curIndex == -1){
                break;
            }
        }
        return path;
    }

    private List<Integer[]> generatePathPool(List<Integer[]> path){
        List<Integer[]> pathPool = new ArrayList<>();
        for(int i = 0; i < roomsCount; i++){
            var neighbours = getNeighbours(i);
            for(int j = 0; j < neighbours.size(); j++){
                Integer[] pairIndex = new Integer[]{i, neighbours.get(j)};
                Arrays.sort(pairIndex);
                if(!isPathContainsPair(pairIndex, path) && !isPathContainsPair(pairIndex, pathPool)){
                    pathPool.add(pairIndex);
                }
            }
        }
        return pathPool;
    }

    private boolean isPathContainsPair(Integer[] pairIndex, List<Integer[]> path){
        boolean isContains = false;
        for(int i = 0; i < path.size(); i++){
            var pathPair = path.get(i);
            if(Objects.equals(pathPair[0], pairIndex[0]) && Objects.equals(pathPair[1], pairIndex[1])){
                isContains = true;
                break;
            }
        }

        return isContains;
    }

    private void addPath(List<Integer[]> path){
        Integer curSize = path.size();
        Integer maxSize = 12;
        Integer randomAdd = (int) (Math.random() * (maxSize - curSize));
        List<Integer[]> pathPool = generatePathPool(path);
        int randomIndex;
        for(int i = 0; i < randomAdd; i++){
            randomIndex = (int) (Math.random() * pathPool.size());
            Integer[] newPath = pathPool.remove(randomIndex);
            path.add(newPath);
        }
    }
}
